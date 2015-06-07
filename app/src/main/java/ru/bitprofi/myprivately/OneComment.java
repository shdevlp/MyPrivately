package ru.bitprofi.myprivately;

public class OneComment {
	private boolean _left;
	private boolean _background;
	private String _comment;

	public OneComment(boolean left, String comment, boolean background) {
		super();
		this._left = left;
		this._comment = comment;
		this._background = background;
	}

	public boolean getLeft() {
		return this._left;
	}

	public String getComment() {
		return this._comment;
	}

	public boolean getBackground() {
		return this._background;
	}
}