package br.com.agenda.bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.com.agenda.dao.EventoDAO;
import br.com.agenda.modelo.Evento;

@ManagedBean //se nao colocar ele assume o nome eventoBean com o primeiro e minusculo
@ViewScoped
public class EventoBean implements Serializable{

	private static final long serialVersionUID = 8458259708861027697L;
	
	private Evento evento;
	private ScheduleModel eventModel;
	private List<Evento> listaEvento;
	private EventoDAO eDao;

	
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	public ScheduleModel getEventModel() {
		return eventModel;
	}
	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}
	public List<Evento> getListaEvento() {
		return listaEvento;
	}
	public void setListaEvento(List<Evento> listaEvento) {
		this.listaEvento = listaEvento;
	}
	public EventoDAO geteDao() {
		return eDao;
	}
	public void seteDao(EventoDAO eDao) {
		this.eDao = eDao;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	@PostConstruct
	public void inicializar(){
		
		eDao = new EventoDAO();
		evento = new Evento();
		eventModel = new DefaultScheduleModel();
		
		try{
			listaEvento = eDao.todosEventos();
			
		}catch(SQLException ex){
			ex.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro no sql"));
		}
		
		for(Evento ev : listaEvento){
			DefaultScheduleEvent evt = new DefaultScheduleEvent();
            evt.setEndDate(ev.getFim());
			evt.setStartDate(ev.getInicio());
			evt.setTitle(ev.getTitulo());
			evt.setData(ev.getId());
			evt.setDescription(ev.getDescricao());
			evt.setAllDay(true); //mostra nos 3 dias o evento 
			evt.setEditable(true);
			eventModel.addEvent(evt);
		}
	}
	

	
	
	public void quandoSelecionado(SelectEvent selectEvent){
		
		ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
		
		for(Evento ev : listaEvento) {
			if(ev.getId() == (Long) event.getData()){
				evento = ev;
				break;
			}
		}
	}
	

	
	public void quandoNovo(SelectEvent selectEvent){
		ScheduleEvent event = new DefaultScheduleEvent("",(Date)selectEvent.getObject(), (Date)selectEvent.getObject());
		evento = new Evento();
        evento.setInicio(new java.sql.Date(event.getStartDate().getTime()));
        evento.setFim(new java.sql.Date(event.getEndDate().getTime()));
	}
	
	
	public void salvar(){
		if(evento.getId() == null){
		   if(evento.getInicio().getTime() <= evento.getFim().getTime()){
			   eDao = new EventoDAO();
			   try{
				   eDao.salvar(evento);
				   inicializar();
			   } catch(SQLException ex){
				   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao salvar evento","Erro: " + ex.getMessage()));
			   }
			   evento = new Evento();
		   } else {
			   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Data inicial","Data inicial: "));   
		   }
		
		} else {
			try{
				eDao.atualizar(evento);
				inicializar();
			}catch(SQLException ex){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao salvar evento","Erro: " + ex.getMessage()));
			}
		}
	}
}
