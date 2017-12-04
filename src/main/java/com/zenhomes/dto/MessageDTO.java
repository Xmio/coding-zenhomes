package com.zenhomes.dto;

import java.awt.TrayIcon.MessageType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MessageDTO {
	private String message;
	private MessageType type;
}