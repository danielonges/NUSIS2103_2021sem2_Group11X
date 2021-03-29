package ejb.session.stateless;

import entity.AppointmentEntity;
import exception.AppointmentNotFoundException;
import java.util.List;

public interface AppointmentEntitySessionBeanLocal {
    
    public Long createAppointmentEntity(AppointmentEntity newAppointmentEntity);
    
    public AppointmentEntity retrieveAppointmentEntityByAppointmentId(Long appointmentId) throws AppointmentNotFoundException;
    
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity);
    
    public void deleteAppointmentEntity(Long appointmentId);

    public List<AppointmentEntity> retrieveAppointmentsByServiceProviderId(Long serviceProviderId) throws AppointmentNotFoundException;
}
