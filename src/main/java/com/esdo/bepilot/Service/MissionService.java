package com.esdo.bepilot.Service;

import com.esdo.bepilot.Model.Entity.Customer;
import com.esdo.bepilot.Model.Entity.Mission;
import com.esdo.bepilot.Model.Request.MissionRequest;
import com.esdo.bepilot.Model.Response.MissionResponse;

import java.util.List;

public interface MissionService {

    List<MissionResponse> getListMission(int pageIndex, int pageSize);

    MissionResponse createMission(MissionRequest missionRequest);

    MissionResponse updateMissionById(Long missionId, Long customerId, MissionRequest missionRequest);

    List<MissionResponse> searchMission(String name,
                                        String communication,
                                        int pageIndex,
                                        int pageSize);

    Customer findCustomerById(Long id);

    List<Mission> findByStatus(String status,Long customerId);

    void deleteMissionById(Long missionId);

}