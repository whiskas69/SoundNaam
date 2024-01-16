package com.example.historyservice6.core.data;

import com.example.historyservice6.core.data.HistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends MongoRepository<HistoryEntity, String> {
    //ไม่ได้ใช้
    List<HistoryEntity> findByUserId(int userId);
    @Query(value="{email:'?0'}")
    List<HistoryEntity> findByEmail(String email);

    @Query(value="{songName:'?0'}")
    List<HistoryEntity> findBySongName(String songName);
}
