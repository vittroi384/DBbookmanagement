package com.libraryManage.DAO;

import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Component;
import java.util.*;

import javax.sql.*;

import com.libraryManage.DTO.*;

@Component
public class BoardDAO {
	private BoardDTO boardDTO;
	private JdbcTemplate jdbcTemplate;

	public BoardDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	public BoardDTO selectByFBID(int inputFBID) {
		try {
			return jdbcTemplate.queryForObject("SELECT * FROM BOARD WHERE FBID=?",
					(rs, rowNum) -> new BoardDTO(rs.getInt("FBID"), rs.getString("EMAIL"), rs.getString("TITLE"),
							rs.getString("CONTENT"), rs.getDate("BOARDDATE"), rs.getInt("BOARDPUBLIC")),
					inputFBID);
		} catch (Exception ex) {
			return null;
		}
	}

	public List<BoardDTO> showAll() {
		List<BoardDTO> result = jdbcTemplate.query("SELECT * FROM BOARD", (rs, rowNum) -> {
			BoardDTO boardDTO = new BoardDTO(rs.getInt("FBID"), rs.getString("EMAIL"), rs.getString("TITLE"),
					rs.getString("CONTENT"), rs.getDate("BOARDDATE"), rs.getInt("BOARDPUBLIC"));
			return boardDTO;
		});
		return result;
	}

	public List<BoardDTO> showVisibleBoard() {
		List<BoardDTO> result = jdbcTemplate.query("SELECT * FROM BOARD WHERE BOARDPUBLIC=1", (rs, rowNum) -> {
			BoardDTO boardDTO = new BoardDTO(rs.getInt("FBID"), rs.getString("EMAIL"), rs.getString("TITLE"),
					rs.getString("CONTENT"), rs.getDate("BOARDDATE"), rs.getInt("BOARDPUBLIC"));
			return boardDTO;
		});
		return result;
	}

	
	
	public List<BoardDTO> showThree() {
		List<BoardDTO> result = jdbcTemplate.query("SELECT * FROM (SELECT * FROM BOARD WHERE boardpublic=1 ORDER BY FBID DESC) WHERE ROWNUM <= 3", (rs, rowNum) -> {
			BoardDTO boardDTO = new BoardDTO(rs.getInt("FBID"), rs.getString("EMAIL"), rs.getString("TITLE"),
					rs.getString("CONTENT"), rs.getDate("BOARDDATE"), rs.getInt("BOARDPUBLIC"));
			return boardDTO;
		});
		return result;
	}

	
	
	public void updatePublic(BoardDTO boardDTO, String newPublic) {
		jdbcTemplate.update("UPDATE BOARD SET boardPUBLIC='" + newPublic + "' WHERE FBID=" + boardDTO.getBoardID() + "");
	}

	
	
	public void insertBoard(String memberEmail, BoardDTO _boardDTO) {
		this.boardDTO = _boardDTO;

		jdbcTemplate.update("INSERT INTO BOARD(FBID, EMAIL, TITLE, CONTENT, BOARDDATE) VALUES(BOARD_SEQ.NEXTVAL, '" + memberEmail + "', '"
				+ boardDTO.getBoardTitle() + "', '" + boardDTO.getBoardContent() + "', sysdate)");
	}

	
	
	public void deleteBoard(int boardID) {
		jdbcTemplate.update("DELETE FROM BOARD WHERE FBID=" + boardID + "");
	}
}
