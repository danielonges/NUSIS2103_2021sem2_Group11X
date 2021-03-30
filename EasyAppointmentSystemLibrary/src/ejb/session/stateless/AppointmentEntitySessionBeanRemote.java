package ejb.session.stateless;

import entity.AppointmentEntity;
import exception.AppointmentNotFoundException;
import java.util.List;

public interface AppointmentEntitySessionBeanRemote {
    
    public Long createAppointmentEntity(AppointmentEntity newAppointmentEntity);
    
    public AppointmentEntity retrieveAppointmentEntityByAppointmentId(Long appointmentId) throws AppointmentNotFoundException;
    
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity);
    
    public void deleteAppointmentEntity(Long appointmentId) throws AppointmentNotFoundException;

    public List<AppointmentEntity> retrieveAppointmentsByServiceProviderId(Long serviceProviderId) throws AppointmentNotFoundException;

    public List<AppointmentEntity> retrieveListOfAppointments() throws AppointmentNotFoundException;
}
