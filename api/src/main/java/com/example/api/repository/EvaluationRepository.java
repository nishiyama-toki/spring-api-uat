// // /src/main/java/com/example/api/repository/EvaluationRepository.java
// package com.example.api.repository;

// import com.example.api.entity.Evaluation;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;

// import java.util.List;

// @Repository
// public interface EvaluationRepository
//     extends JpaRepository<Evaluation, Integer> {

//   /**
//    * 従来のメソッド（こちらはそのままでも良い）
//    */
//   List<Evaluation> findByPhase_IdAndTarget_Id(Integer phaseId, Integer targetId);

//   /**
//    * @QueryアノテーションでJPQLクエリを直接記述する
//    * e.phase.id は Evaluationエンティティのphaseフィールド(Phase型)のidプロパティを指す
//    */
//   @Query("SELECT e FROM Evaluation e WHERE e.phase.id = :phaseId AND e.target.id = :targetId")
//   List<Evaluation> findByPhaseAndTarget(
//       @Param("phaseId") Integer phaseId,
//       @Param("targetId") Integer targetId
//   );
// }


package com.example.api.repository;

import com.example.api.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository 
        extends JpaRepository<Evaluation, Integer> {

        // 「過去評価履歴」機能で使われているメソッド
        List<Evaluation> findByPhase_IdAndTarget_Id(Integer phaseId, Integer targetId);

        // 「過去評価履歴」機能で使われているメソッド
        @Query("SELECT e FROM Evaluation e WHERE e.phase.id = :phaseId AND e.target.id = :targetId")
        List<Evaluation> findByPhaseAndTarget (
            @Param("phaseId") Integer phaseId,
            @Param("targetId") Integer targetId
        );

        // 「自己評価」機能のために追加したメソッド
        Optional<Evaluation> findByEvaluator_IdAndTarget_IdAndPhase_Id (
            Integer evaluatorId,
            Integer targetId,
            Integer phaseId
        );
}
