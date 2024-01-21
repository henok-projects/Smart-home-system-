package com.galsie.gcs.testservice;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.testservice.entity.inheritence.SomeOtherTopic;
import com.galsie.gcs.testservice.entity.inheritence.SomeTopic;
import com.galsie.gcs.testservice.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class TestserviceApplicationTests {

	@Autowired
	TopicRepository topicRepository;

	@Test
	void contextLoads() {
	}

	@Test
	public void createTopics(){
		GCSResponse.saveEntity(topicRepository, SomeTopic.builder().someTopicName("yourmom").build());
		GCSResponse.saveEntity(topicRepository, SomeTopic.builder().someTopicName("yourfatmom").build());
		GCSResponse.saveEntity(topicRepository, SomeOtherTopic.builder().otherTopic("yourothermom").build());
		GCSResponse.saveEntity(topicRepository, SomeOtherTopic.builder().otherTopic("yourotherfatmom").build());

	}

	@Test
	public void loadAllTopics(){
		var allTopics = topicRepository.findAll();
		assert allTopics.size() > 0;
	}

	@Test
	public void loadSomeTopicsExcept(){
		var someTopics = topicRepository.findAllByUniqueIdsNotMatching(Arrays.asList(0L, 1L));
		assert someTopics.size() > 0;
	}
}
