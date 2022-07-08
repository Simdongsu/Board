package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {
    //글목록
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
//    @Query(value = "select b.*, m.name " +
//            " from board b left join member m" +
//            " on b.writer_email=m.email " +
//            " where b.bno =:bno",nativeQuery = true)
    Object getBoardWithWriter(@Param("bno") Long bno);

    //댓글목록
    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
//    @Query("SELECT b, r FROM Reply r LEFT JOIN r.board b WHERE b.bno = :bno")
//    @Query(value = "SELECT b.bno, r.* " +
//            " FROM Board b LEFT JOIN Reply r " +
//            " ON r.board_bno = b.bno " +
////            " WHERE b.bno = :bno",nativeQuery = true)
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    //댓글갯수
    @Query(value ="SELECT b, w, count(r) " +
            " FROM Board b " +
            " LEFT JOIN b.writer w " +
            " LEFT JOIN Reply r ON r.board = b " +
            " GROUP BY b",
            countQuery ="SELECT count(b) FROM Board b")
    //실행안됨
//    @Query(value ="SELECT b, w, count(r) " +
//            " FROM Reply r " +
//            " LEFT JOIN r.board b " +
//            " LEFT JOIN b.writer w  " +
//            " GROUP BY b",
//            countQuery ="SELECT count(b) FROM Board b")
//    @Query(value ="SELECT b.*, m.name, count(r.rno) " +
//            " FROM board b  LEFT JOIN member m on b.writer_email=m.email" +
//            " LEFT JOIN reply r ON r.board_bno = b.bno " +
//            " GROUP BY b.bno",
//            countQuery ="SELECT count(*) FROM Board b",nativeQuery = true)
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    //상세보기
    @Query("SELECT b, w, count(r) " +
            " FROM Board b LEFT JOIN b.writer w " +
            " LEFT OUTER JOIN Reply r ON r.board = b" +
            " WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

}
