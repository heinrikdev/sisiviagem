package ufg.br.fabricasw.jatai.swsisviagem.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * @author Ronaldo N Sousa
 * Criado em: 06/08/2019 10:30
 **/
@Configuration
@EnableAsync
public class TaskExecutorConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
