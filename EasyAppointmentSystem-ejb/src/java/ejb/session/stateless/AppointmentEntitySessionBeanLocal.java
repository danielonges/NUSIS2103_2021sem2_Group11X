package ejb.session.stateless;

import entity.AppointmentEntity;

public interface AppointmentEntitySessionBeanLocal {
    
    public Long createAppointmentEntity(AppointmentEntity newAppointmentEntity);
    
    public AppointmentEntity retrieveAppointmentEntityByAppointmentId(Long appointmentId);
    
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity);
    
    public void deleteAppointmentEntity(Long appointmentId);
}
