package ru.bitprofi.myprivately;

public class OneComment {
	private boolean m_left;
	private String m_comment;

	public OneComment(boolean left, String comment) {
		super();
		this.m_left = left;
		this.m_comment = comment;
	}

	public boolean getLeft() {
		return this.m_left;
	}

	public String getComment() {
		return this.m_comment;
	}
}