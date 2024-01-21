package com.galsie.gcs.testservice.entity.inheritence;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SomeOtherTopic extends TopicEntity {
    @Builder
    public SomeOtherTopic(Long id, String otherTopic) {
        super(id);
        this.otherTopic = otherTopic;
    }

    @Column(name="other_topic_name")
    String otherTopic;
}

