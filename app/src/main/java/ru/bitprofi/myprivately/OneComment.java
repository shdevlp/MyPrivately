package ru.bitprofi.myprivately;

public class OneComment {
	private boolean _left;
	private String _comment;

	public OneComment(boolean left, String comment) {
		super();
		this._left = left;
		this._comment = comment;
	}

	public boolean getLeft() {
		return this._left;
	}

	public String getComment() {
		return this._comment;
	}
}