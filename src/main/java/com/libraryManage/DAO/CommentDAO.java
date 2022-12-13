package com.libraryManage.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.*;
import com.libraryManage.DTO.*;
import java.util.*;

import javax.sql.*;

@Component
public class CommentDAO {
	private CommentDTO commentDTO;
	private JdbcTemplate jdbcTemplate;

	public CommentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}

	public List<CommentDTO> selectByFBID(int inputFBID) {
		List<CommentDTO> result = jdbcTemplate.query("SELECT * FROM BOARDCOMMENT WHERE FBID=" + inputFBID + "",
				(rs, rowNum) -> {
					CommentDTO commentDTO = new CommentDTO(rs.getInt("CID"), rs.getInt("FBID"), rs.getString("NAME"),
							rs.getString("CONTENT"));
					return commentDTO;
				});
		return result;
	}

	public int findLatestCID(int inputFBID) {
		try {
			CommentDTO _commentDTO = jdbcTemplate.queryForObject(
					"SELECT * FROM (SELECT * FROM BOARDCOMMENT WHERE FBID=? ORDER BY CID DESC) WHERE ROWNUM <= 1",
					(rs, rowNum) -> new CommentDTO(rs.getInt("CID"), rs.getInt("FBID"), rs.getString("NAME"),
							rs.getString("CONTENT")),
					inputFBID);
			
			if(_commentDTO == null) {
				throw new Exception();
			} else {
				this.commentDTO = _commentDTO;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}

		return commentDTO.getCommentID();
	}

	public void insertComment(CommentDTO _commentDTO) {
		jdbcTemplate.update("INSERT INTO BOARDCOMMENT(CID, FBID, NAME, CONTENT) VALUES(" + _commentDTO.getCommentID() + ", "
				+ _commentDTO.getCommentBoardID() + ", '" + _commentDTO.getCommentName() + "', '"
				+ _commentDTO.getCommentContent() + "')");
	}
}
