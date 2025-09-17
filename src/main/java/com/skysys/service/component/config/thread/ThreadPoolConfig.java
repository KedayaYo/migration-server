package com.skysys.service.component.config.thread;

import com.skysys.service.component.config.thread.properties.ThreadPoolProperties;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author tth
 * @description 线程池配置类，提供本地线程池和虚拟线程池的配置选项
 */
@Slf4j
@Order(1)
@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolConfig {

    /**
     * 核心线程数为 CPU 核心数 + 1
     */
    private final int core = Runtime.getRuntime().availableProcessors() + 1;
    private ThreadPoolTaskExecutor executor;
    private ExecutorService virtualExecutor;

    /**
     * 配置线程池的Bean
     *
     * @param threadPoolProperties 从配置文件中读取线程池相关属性
     * @return Executor 本地线程池或虚拟线程池
     */
    @Bean(name = "threadPoolTaskExecutor")
    // 如果配置文件中 thread-pool.enabled=true，则创建线程池
    @ConditionalOnProperty(prefix = "thread-pool", name = "enabled", havingValue = "true")
    public Executor threadPoolTaskExecutor(ThreadPoolProperties threadPoolProperties) {

        // 检查是否使用虚拟线程池
        if (threadPoolProperties.isUseVirtualThreads()) {
            log.info("======= 启用虚拟线程池 =======");
            virtualExecutor = Executors.newVirtualThreadPerTaskExecutor(); // 使用虚拟线程池
            return virtualExecutor;
        } else {
            log.info("本地线程池配置初始化 ==> cpu 核心数：{}", Optional.of(core));
            executor = new ThreadPoolTaskExecutor();
            // 配置核心线程数
            executor.setCorePoolSize(core);
            // 配置最大线程数
            executor.setMaxPoolSize(core * 3);
            // 配置队列大小
            executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
            // 配置线程池中的线程的最大空闲时间
            executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
            // 配置线程池中的线程的名称前缀
            executor.setThreadNamePrefix("sky-task-");
            executor.setWaitForTasksToCompleteOnShutdown(true);
            executor.setAwaitTerminationSeconds(5);
            // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
            // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            // 执行初始化
            executor.initialize();
            return executor;
        }
    }

    /**
     * 销毁事件
     */
    @PreDestroy
    public void destroy() {
        try {
            log.info("====关闭后台任务任务线程池====");
            if (executor != null) {
                executor.shutdown();
            }
            if (virtualExecutor != null) {
                virtualExecutor.shutdown();
            }
            // Threads.shutdownAndAwaitTermination(executor.getThreadPoolExecutor());
        } catch (Exception e) {
            log.error("线程池关闭时出错", e);
        }
    }

}
