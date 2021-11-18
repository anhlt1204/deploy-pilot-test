package com.esdo.bepilot.Controller;

import com.esdo.bepilot.Model.Entity.Mission;
import com.esdo.bepilot.Model.Request.MissionRequest;
import com.esdo.bepilot.Model.Response.MissionResponse;
import com.esdo.bepilot.Service.Implement.MissionServiceImpl;
import com.esdo.bepilot.Service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("missions")
public class MissionController {

    @Autowired
    MissionService missionService;

    @GetMapping
    public ResponseEntity<List<MissionResponse>> getListCollateral(@RequestParam(value = "pageIndex", defaultValue = "0",
            required = false) int pageIndex,
                                                                   @RequestParam(value = "pageSize", defaultValue = "10",
                                                                           required = false) int pageSize) {
        return ResponseEntity.ok().body(missionService.getListMission(pageIndex, pageSize));
    }

    @GetMapping({"/search"})
    public ResponseEntity<List<MissionResponse>> searchMission(@RequestParam(value = "name", required = false) String name,
                                                               @RequestParam(value = "communication", required = false) String communication,
                                                               @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        List<MissionResponse> searchMission = missionService.searchMission(name, communication, pageIndex, pageSize);
        return ResponseEntity.ok(searchMission);
    }

    @GetMapping("/status")
    public ResponseEntity<List<Mission>> getListMissionStatus(@RequestParam(value = "name") String status,
                                                              @RequestParam(value = "customer") Long customerId) {
        return ResponseEntity.ok().body(missionService.findByStatus(status, customerId));
    }

    @PostMapping
    public ResponseEntity<MissionResponse> createdMission(@RequestBody @Valid MissionRequest missionRequest) {
        return ResponseEntity.ok().body(missionService.createMission(missionRequest));
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<MissionResponse> updateMission(@PathVariable(value = "id") Long missionId,
                                                         @RequestParam Long customerId,
                                                         @RequestBody @Valid MissionRequest missionRequest) {
        return ResponseEntity.ok().body(missionService.updateMissionById(missionId, customerId, missionRequest));
    }

    @DeleteMapping("/{missionId}")
    public void deleteMission(@PathVariable Long missionId) {
        missionService.deleteMissionById(missionId);
    }
}
