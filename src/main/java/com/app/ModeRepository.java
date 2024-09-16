package com.app;

import com.app.entity.Mode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeRepository extends JpaRepository<Mode, Long> {
    Mode findFirstByField1AndField2AndField3(String field1, String field2, String field3);
}
