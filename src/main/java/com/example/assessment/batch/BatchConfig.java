package com.example.assessment.batch;

import com.example.assessment.entity.Record;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public FlatFileItemReader<Record> reader() {
        return new FlatFileItemReaderBuilder<Record>()
                .name("recordItemReader")
                .resource(new ClassPathResource("input.txt"))
                .delimited()
                .names("customerId", "accountNumber", "description")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Record.class);
                }})
                .build();
    }

    @Bean
    public JpaItemWriter<Record> writer(EntityManagerFactory emf) {
        return new org.springframework.batch.item.database.builder.JpaItemWriterBuilder<Record>()
                .entityManagerFactory(emf)
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, FlatFileItemReader<Record> reader,
                      JpaItemWriter<Record> writer) {
        return stepBuilderFactory.get("step1")
                .<Record, Record>chunk(10)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importUserJob(JobBuilderFactory jobBuilderFactory, Step step1) {
        return jobBuilderFactory.get("importRecordJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }
}
