package ejb.session.stateless;

import entity.AppointmentEntity;
import util.exception.AppointmentNotFoundException;
import java.util.List;
import util.exception.CreateNewAppointmentEntityException;
import util.exception.CustomerNotFoundException;
import util.exception.UnauthorisedOperationException;

public interface AppointmentEntitySessionBeanLocal {
    
    public Long createAppointmentEntity(Long customerId, Long providerId, AppointmentEntity newAppointmentEntity) throws CreateNewAppointmentEntityException;
    
    public AppointmentEntity retrieveAppointmentEntityByAppointmentId(Long appointmentId) throws AppointmentNotFoundException;
    
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity);
    
    public void deleteAppointmentEntity(Long appointmentId) throws AppointmentNotFoundException;
    
    public AppointmentEntity retrieveAppointmentEntityByAppointmentNo(Long appointmentNo) throws AppointmentNotFoundException;
    
    public void cancelAppointmentByCustomerId(Long customerId, Long appointmentNo) throws CustomerNotFoundException, AppointmentNotFoundException, UnauthorisedOperationException;
}
