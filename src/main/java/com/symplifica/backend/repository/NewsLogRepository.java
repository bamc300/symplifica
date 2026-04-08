package com.symplifica.backend.repository;

import com.symplifica.backend.entity.NewsLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NewsLogRepository extends JpaRepository<NewsLog, UUID> {
}
