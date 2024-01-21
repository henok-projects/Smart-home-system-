package com.galsie.gcs.testservice.entity.inheritence;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class SomeTopic extends TopicEntity {
    @Builder
    public SomeTopic(Long id, String someTopicName) {
        super(id);
        this.someTopicName = someTopicName;
    }

    @Column(name="Some_topic_name")
    String someTopicName;
}
