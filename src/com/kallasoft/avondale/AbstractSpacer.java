package com.kallasoft.avondale;

public abstract class AbstractSpacer implements Spacer
{
	private double topSpace = 0;
	private double bottomSpace = 0;
	private double leftSpace = 0;
	private double rightSpace = 0;

	public AbstractSpacer(double topSpace, double bottomSpace,
			double leftSpace, double rightSpace)
	{
		setSpaces(topSpace, bottomSpace, leftSpace, rightSpace);
	}

	public boolean isEmpty()
	{
		return (getTopSpace() == 0 && getBottomSpace() == 0
				&& getLeftSpace() == 0 && getRightSpace() == 0);
	}

	public double getTopSpace()
	{
		return topSpace;
	}

	public void setTopSpace(double topSpace)
	{
		if (topSpace < 0)
			throw new IllegalArgumentException("topSpace (" + topSpace + ") must be >= 0");

		this.topSpace = topSpace;
	}

	public double getBottomSpace()
	{
		return bottomSpace;
	}

	public void setBottomSpace(double bottomSpace)
	{
		if (bottomSpace < 0)
			throw new IllegalArgumentException("bottomSpace (" + bottomSpace + ") must be >= 0");

		this.bottomSpace = bottomSpace;
	}

	public double getLeftSpace()
	{
		return leftSpace;
	}

	public void setLeftSpace(double leftSpace)
	{
		if (leftSpace < 0)
			throw new IllegalArgumentException("leftSpace (" + leftSpace + ") must be >= 0");

		this.leftSpace = leftSpace;
	}

	public double getRightSpace()
	{
		return rightSpace;
	}

	public void setRightSpace(double rightSpace)
	{
		if (rightSpace < 0)
			throw new IllegalArgumentException("rightSpace (" + rightSpace + ") must be >= 0");

		this.rightSpace = rightSpace;
	}

	public void getSpaces(Spacer spacer)
	{
		spacer.setSpaces(this);
	}

	public void setSpaces(double topSpace, double bottomSpace,
			double leftSpace, double rightSpace)
	{
		setTopSpace(topSpace);
		setBottomSpace(bottomSpace);
		setLeftSpace(leftSpace);
		setRightSpace(rightSpace);
	}

	public void setSpaces(Spacer spacer)
	{
		setSpaces(spacer.getTopSpace(), spacer.getBottomSpace(), spacer
				.getLeftSpace(), spacer.getRightSpace());
	}
}