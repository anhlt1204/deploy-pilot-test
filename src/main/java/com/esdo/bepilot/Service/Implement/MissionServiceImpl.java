package com.esdo.bepilot.Service.Implement;

import com.esdo.bepilot.Exception.CustomException;
import com.esdo.bepilot.Exception.InvalidException;
import com.esdo.bepilot.Model.Entity.Customer;
import com.esdo.bepilot.Model.Entity.Employee;
import com.esdo.bepilot.Model.Entity.Mission;
import com.esdo.bepilot.Model.Request.MissionRequest;
import com.esdo.bepilot.Model.Response.MissionResponse;
import com.esdo.bepilot.Repository.CustomerRepository;
import com.esdo.bepilot.Repository.MissionRepository;
import com.esdo.bepilot.Service.Mapper.ConvertObject;
import com.esdo.bepilot.Service.Mapper.MissionMapper;
import com.esdo.bepilot.Service.MissionService;
import com.esdo.bepilot.Service.Validate.MissionValidate;
import com.esdo.bepilot.Specification.MissionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.PreUpdate;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MissionServiceImpl implements MissionService {

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    MissionMapper missionMapper;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    MissionValidate missionValidate;

    /**
     * Lấy danh sách các nhiệm vụ
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<MissionResponse> getListMission(int pageIndex, int pageSize) {
        Pageable paging = PageRequest.of(pageIndex, pageSize);
        Page<Mission> page = missionRepository.findAll(paging);
        List<Mission> missions = page.getContent();
        return missionMapper.mapToListResponse(missions);
    }

    /**
     * Tạo nhiệm vụ mới
     *
     * @param missionRequest
     * @return
     */
    @Override
    public MissionResponse createMission(MissionRequest missionRequest) {
        //validate đầu vào
        missionValidate.validate(missionRequest);

        Mission mission = missionMapper.mapToMission(missionRequest);
        missionRepository.save(mission);

        List<Mission> missions = missionRepository.getMissionKey();
        missions.get(0).setMissionKey("NV" + missions.get(0).getId());
        Mission missionCreated = missionRepository.save(missions.get(0));

        return missionMapper.mapToMissionResponse(missionCreated);
    }


    /**
     * Sửa nhiệm vụ theo id và check id theo id của khác hàng
     *
     * @param missionId
     * @param customerId
     * @param missionRequest
     * @return
     */
    @Override
    public MissionResponse updateMissionById(Long missionId, Long customerId, MissionRequest missionRequest) {
        //validate đầu vào
        missionValidate.validate(missionRequest);
        Mission mission = missionRepository.findById(missionId).get();
        OffsetDateTime time = mission.getCreateAt();
        Optional<Mission> optionalMission = missionRepository.findById(missionId);

        if (optionalMission.isEmpty()) {
            throw new InvalidException("Invalid mission has id = " + missionId);
        }
        missionRequest.setId(missionId);
        missionRequest.setCustomerId(customerId);
        missionRequest.setUpdateAt(OffsetDateTime.now());
        Mission missionMap = missionMapper.mapToMission(missionRequest);
        missionMap.setCreateAt(time);
        Mission missionEdit = missionRepository.save(missionMap);

        return missionMapper.mapToMissionResponse(missionEdit);
    }

    /**
     * Tìm kiếm theo tên nhiệm vụ và nền tảng
     *
     * @param name
     * @param communication
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<MissionResponse> searchMission(String name, String communication, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Mission> page = missionRepository.findAll(MissionSpecification.filterMission(name, communication), pageable);
        List<Mission> missions = page.getContent();
        return missionMapper.mapToListResponse(missions);
    }

    /**
     * check id khách hàng có tồn tại không
     *
     * @param id
     * @return
     */
    @Override
    public Customer findCustomerById(Long id) {
        Optional<Customer> opt = customerRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new CustomException("Customer not found");
    }

    /**
     * Lấy danh sach nhiệm vụ theo trạng thái
     *
     * @param status
     * @param customerId
     * @return
     */
    @Override
    public List<Mission> findByStatus(String status, Long customerId) {
        List<MissionResponse> mission = missionRepository.findByStatus(status, customerId);
        return missionMapper.map(mission);
    }

    /**
     * Xóa nhiệm vụ theo id nhiệm vụ
     *
     * @param missionId
     */
    @Override
    public void deleteMissionById(Long missionId) {
        Optional<Mission> optionalMission = missionRepository.findById(missionId);

        if (optionalMission.isEmpty()) {
            throw new InvalidException("Invalid mission has id = " + missionId);
        }
        missionRepository.deleteById(missionId);
    }

}