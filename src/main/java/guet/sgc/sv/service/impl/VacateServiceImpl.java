package guet.sgc.sv.service.impl;

import guet.sgc.sv.dao.VacateMapper;
import guet.sgc.sv.entity.Vacate;
import guet.sgc.sv.entity.VacateExample;
import guet.sgc.sv.service.VacateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VacateServiceImpl implements VacateService {
    @Autowired
    private VacateMapper vacateMapper;

    @Override
    public Vacate getVacate(String username, Date date) {
        VacateExample example = new VacateExample();
        VacateExample.Criteria criteria = example.createCriteria();
        criteria.andStudentIdEqualTo(username);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = null;
        try {
            myDate = simpleDateFormat.parse(simpleDateFormat.format(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        criteria.andSubmitTimeGreaterThanOrEqualTo(myDate);
        criteria.andSubmitTimeLessThanOrEqualTo(new Date(myDate.getTime()+86400000-1));
        List<Vacate> vacateList = vacateMapper.selectByExample(example);
        if (vacateList.size() != 0){
            return vacateList.get(0);
        }
        return null;
    }

    @Transactional
    @Override
    public void saveVacate(Vacate vacate) {
        vacateMapper.insertSelective(vacate);
    }

    @Override
    public List<Vacate> getVacateList(String username, Date startTime, Date endTime) {
        VacateExample example = new VacateExample();
        example.setOrderByClause("submit_time desc");
        VacateExample.Criteria criteria = example.createCriteria();
        criteria.andStudentIdLike(username + "%");
        criteria.andLeaveStatusEqualTo(0);
        if (startTime != null){
            criteria.andSubmitTimeGreaterThanOrEqualTo(startTime);
        }
        if (endTime != null){
            criteria.andSubmitTimeLessThanOrEqualTo(endTime);
        }
        return vacateMapper.selectByExample(example);
    }

    @Override
    public Vacate getVacate(Long leaveId) {
        return vacateMapper.selectByPrimaryKey(leaveId);
    }

    @Transactional
    @Override
    public void editVacate(Vacate vacate) {
        vacateMapper.updateByPrimaryKeySelective(vacate);
    }

    @Transactional
    @Override
    public void delVacate(Long leaveId) {
        Vacate vacate = vacateMapper.selectByPrimaryKey(leaveId);
        vacate.setLeaveStatus(1);
        vacateMapper.updateByPrimaryKeySelective(vacate);
    }

    @Override
    public List<Vacate> getVacateList(String classId, String studentId, Date startTime, Date endTime) {
        VacateExample example = new VacateExample();
        example.setOrderByClause("submit_time desc");
        VacateExample.Criteria criteria = example.createCriteria();
        criteria.andLeaveStatusEqualTo(0);
        if (studentId != null && !studentId.trim().equals("")){
            criteria.andStudentIdEqualTo(studentId);
        }else if (classId != null && !classId.trim().equals("")){
            criteria.andStudentIdLike(classId+"%");
        }
        if (startTime != null){
            criteria.andSubmitTimeGreaterThanOrEqualTo(startTime);
        }
        if (endTime != null){
            criteria.andSubmitTimeLessThanOrEqualTo(endTime);
        }
        return vacateMapper.selectByExample(example);
    }

    @Transactional
    @Override
    public void delBatchVacate(String id_str) {
        String[] ids = id_str.split(",");
        ArrayList<Long> idList = new ArrayList<>();
        for (String id : ids){
            idList.add(Long.parseLong(id));
        }
        for(Long leaveId : idList){
           Vacate vacate = vacateMapper.selectByPrimaryKey(leaveId);
           vacate.setLeaveStatus(1);
           vacateMapper.updateByPrimaryKeySelective(vacate);
       }
    }

    @Override
    public Vacate getVacate(String username) {
        VacateExample example = new VacateExample();
        VacateExample.Criteria criteria = example.createCriteria();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = null;
        try {
            myDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        }catch (Exception e){
            e.printStackTrace();
        }
        criteria.andSubmitTimeGreaterThanOrEqualTo(myDate);
        criteria.andSubmitTimeLessThanOrEqualTo(new Date(myDate.getTime()+86400000-1));
        criteria.andStudentIdEqualTo(username);
        criteria.andLeaveStatusEqualTo(1);
        List<Vacate> vacateList = vacateMapper.selectByExample(example);
        if (vacateList.size() != 0){
            return vacateList.get(0);
        }
        return null;
    }

    @Override
    public List<Vacate> getVacateList(String username) {
        VacateExample example = new VacateExample();
        example.setOrderByClause("submit_time desc");
        VacateExample.Criteria criteria = example.createCriteria();
        criteria.andStudentIdEqualTo(username);
        return vacateMapper.selectByExample(example);
    }
}
