package com.eipulse.teamproject.schedule;

import com.eipulse.teamproject.entity.employee.Employee;
import com.eipulse.teamproject.entity.formapproval.FormRecord;
import com.eipulse.teamproject.repository.employeerepository.EmployeeRepository;
import com.eipulse.teamproject.repository.formapprovalrepository.FormRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CheckOutReminderTask {

    @Autowired
    private FormRecordRepository frRepo;
    @Autowired
    private EmployeeRepository empRepo;
    @Autowired
    private TaskScheduler taskScheduler;

    @Scheduled(cron = "0 0 0 * * ?") // 每天凌晨0點觸發
    public void checkForFormRecords() {
        LocalDate now = LocalDate.now();
        LocalDateTime start = now.atStartOfDay();
        LocalDateTime end = now.plusDays(1).atStartOfDay();
        List<FormRecord> records = frRepo.findFormRecordDate(start, end);
        if(records.size()>0){
            for(int i=0; i<records.size(); i++){
                FormRecord record = records.get(i);
                // 任務定時
                taskScheduler.schedule(new RemindTask(record, frRepo), new CronTrigger("0 " + records.get(i).getTerminationDate().getMinute() +
            " " + records.get(i).getTerminationDate().getHour() + " " +
            records.get(i).getTerminationDate().getDayOfMonth() + " " +
            records.get(i).getTerminationDate().getMonth().getValue() + " ?"));
            }
        }
        List<FormRecord> lerecords = frRepo.findLeaveResignation(now);
        if(lerecords.size()>0){
            List<Employee> employees = new ArrayList<>();
            for(FormRecord record:lerecords){
                Employee emp = record.getEmployee();
                emp.setEmpState("離職");
                employees.add(emp);
            }
            empRepo.saveAll(employees);
        }

    }

    class RemindTask implements Runnable {
        private FormRecord record;

        private FormRecordRepository frRepo;

        public RemindTask(FormRecord record, FormRecordRepository frRepo) {
            this.record = record;
            this.frRepo = frRepo;
        }
        @Override
        public void run() {
            //設定表單過期
            record.setStatusId(5);
            frRepo.save(record);
        }
    }

}
