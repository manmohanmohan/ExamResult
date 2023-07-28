package com.db.dataplatform.techtest.server.persistence.repository;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface DataStoreRepository extends JpaRepository<DataBodyEntity, Long> {


   Optional<List<DataBodyEntity>> findByDataHeaderEntity_dataHeaderId(Long aLong);

   Optional<List<DataBodyEntity>> findByDataHeaderEntity_blocktype(BlockTypeEnum blockTypeEnum);
}
