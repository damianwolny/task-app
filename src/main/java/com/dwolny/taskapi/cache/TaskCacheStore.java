package com.dwolny.taskapi.cache;

import com.dwolny.taskapi.model.Task;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TaskCacheStore {

	private Cache<String, Task> cache;

	@PostConstruct
	public void initCache(){
		this.cache = CacheBuilder.newBuilder()
				.maximumSize(10_000)
				.expireAfterWrite(10, TimeUnit.MINUTES)
				.build();
	}

	public Optional<Task> getTask(String id) {
		Task task = cache.getIfPresent(id);
		return Optional.ofNullable(task);
	}

	public void update(String id, Task task) {
		log.info("[CACHE] Putting key '{}' in cache", id);
		this.cache.put(id, task);
	}
}