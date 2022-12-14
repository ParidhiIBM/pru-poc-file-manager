package com.fileManager.fileManager.jwt;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogoutUserToken {
	
	private String tId;
	private boolean logout;
	private String token;

}
