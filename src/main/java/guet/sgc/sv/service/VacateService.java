package guet.sgc.sv.service;

import guet.sgc.sv.entity.Vacate;

import java.util.Date;
import java.util.List;

public interface VacateService {
    Vacate getVacate(String username, Date date);

    void saveVacate(Vacate vacate);

    List<Vacate> getVacateList(String username, Date startTime, Date endTime);

    Vacate getVacate(Long leaveId);

    void editVacate(Vacate vacate);

    void delVacate(Long leaveId);

    List<Vacate> getVacateList(String classId, String studentId, Date startTime, Date endTime);

    void delBatchVacate(String id_str);

    Vacate getVacate(String username);

    List<Vacate> getVacateList(String username);
}
