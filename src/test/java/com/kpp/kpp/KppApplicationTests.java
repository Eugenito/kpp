package com.kpp.kpp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class KppApplicationTests {

	@Autowired
	CopyController copyController;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp(){
		this.mockMvc = MockMvcBuilders.standaloneSetup(copyController).build();
	}

	@Test
	public void test_successfulResult_correctRequest() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/copy?value=dadadadada")).andReturn();
		assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void test_badRequest_wrongRequest() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/copy?value=  ")).andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void test_successResult() {
		String value = "test";
		Fields actual = copyController.copyValue(value);
		assertEquals(value, actual.getField2(), actual.getField3());
	}
}
