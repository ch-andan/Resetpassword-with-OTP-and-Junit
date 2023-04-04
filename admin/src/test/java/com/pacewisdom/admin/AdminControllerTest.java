package com.pacewisdom.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacewisdom.admin.Entity.*;
import com.pacewisdom.admin.dto.*;
import com.pacewisdom.admin.exception.PWSException;
import com.pacewisdom.admin.repository.ModelRepo;
import com.pacewisdom.admin.repository.RoleRepo;
import com.pacewisdom.admin.repository.UserRepo;
import com.pacewisdom.admin.repository.UserRoleXrefRepo;
import com.pacewisdom.admin.service.AdminService;
import com.pacewisdom.admin.utility.DateUtils;
import com.pacewisdom.admin.utility.JsonUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AdminService adminService;

    User RECORD_1 = new User("122", "c", "c", new Date(12, 10, 1999), "cc123@gmail.com", "9900245616", "Chandan@564", true, "sdfghjkdfgh", "dfghdfgh");

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private static final String SIGNUP_API = "http://localhost:9191/admin/public/signup";
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private ModelRepo modelRepo;

    @Autowired
    private UserRepo repo;

    @Autowired
    UserRoleXrefRepo userRoleXrefRepo;

    @Test
    public void testFetchAllUsers() throws Exception {
        List<User> userList = Collections.singletonList(RECORD_1);
        Mockito.when(adminService.fetchAllUser()).thenReturn(userList);
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(1)));
    }

    @Test
    public void testSignup() throws Exception {
        Signupdto signupdto = new Signupdto();
        signupdto.setFirstName("Test");
        signupdto.setLastName("User");
        signupdto.setEmail("testuser@example.com");
        signupdto.setDateOfBirth("01/01/2000");
        signupdto.setPhoneNumber("1234567890");
        signupdto.setPassword("Test@123");

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setFirstName(signupdto.getFirstName());
        user.setLastName(signupdto.getLastName());
        user.setEmail(signupdto.getEmail());
        user.setDateOfBirth(DateUtils.getUtilDateFromString(signupdto.getDateOfBirth()));
        user.setPhoneNumber(signupdto.getPhoneNumber());
        user.setIsActive(true);
        user.setPassword(new BCryptPasswordEncoder(8).encode(signupdto.getPassword()));


        when((Collection) adminService.userSignUp(any(Signupdto.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_API).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(signupdto))).andExpect(status().isCreated()).andReturn();
    }

    @Test
    public void testAddRole() throws Exception {
        Roledto roledto = new Roledto();
        roledto.setName("ROLE_TEST");
        roledto.setIsActive(true);

        Role role = new Role();
        role.setId(UUID.randomUUID().toString());
        role.setName(roledto.getName());
        role.setIsActive(true);

        when((Collection) adminService.addrole(any(Roledto.class))).thenReturn(role);


        mockMvc.perform(MockMvcRequestBuilders.post("/admin/add-role").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(roledto))).andExpect(status().isCreated()).andReturn();
    }

    @Test
    public void testFetchRoleById_success() throws Exception {
        Role role = new Role();
        role.setId("01a5b37b-747b-4789-9502-c15bf66167b1");
        role.setName("employee");
        role.setIsActive(true);
        RoleRepo roleRepo = Mockito.mock(RoleRepo.class);
        Mockito.when(roleRepo.findById("01a5b37b-747b-4789-9502-c15bf66167b1")).thenReturn(Optional.of(role));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/role/id").param("id", "01a5b37b-747b-4789-9502-c15bf66167b1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testAddModel() throws Exception {
        ModelDto modelDto = new ModelDto();
        modelDto.setName("Model_TEST");
        modelDto.setIsActive(true);

        Model model = new Model();
        model.setId(UUID.randomUUID().toString());
        model.setName(modelDto.getName());
        model.setIsActive(true);

        when((Collection) adminService.addrole(any(Roledto.class))).thenReturn(model);


        mockMvc.perform(MockMvcRequestBuilders.post("/admin/addmodel").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(modelDto))).andExpect(status().isOk()).andReturn();
    }
    @Test
    public void testAddPermission() throws Exception {
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setIsActive(true);
        permissionDto.setIsAdd(true);
        permissionDto.setIsDelete(true);
        permissionDto.setIsUpdate(true);
        permissionDto.setIsView(true);
        permissionDto.setModelId("7a438b6d-c143-4b4b-89c1-0721d39ed110");
        permissionDto.setRoleId("01a5b37b-747b-4789-9502-c15bf66167b1");
        Permission permission=new Permission();
        permission.setId(UUID.randomUUID().toString());
        permission.setIsAdd(permissionDto.getIsAdd());
        permission.setIsActive(permissionDto.getIsActive());
        permission.setIsDelete(permissionDto.getIsDelete());
        permission.setIsUpdate(permissionDto.getIsUpdate());
        permission.setIsView(permissionDto.getIsView());
        Optional<Model> optionalModel = modelRepo.findById(permissionDto.getModelId());
        if (optionalModel.isPresent()) {
            permission.setModel(optionalModel.get());
        } else {
            throw new PWSException("Model id not found");
        }
        Optional<Role> optionalRole = roleRepo.findById(permissionDto.getRoleId());
        if (optionalRole.isPresent()) {
            permission.setRole(optionalRole.get());
        }

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/addpermission").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(permissionDto))).andExpect(status().isOk()).andReturn();
    }

}

