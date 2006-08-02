package com.kallasoft.avondale;

public interface Spacer
{
	public boolean isEmpty();

	public double getTopSpace();

	public void setTopSpace(double topSpace);

	public double getBottomSpace();

	public void setBottomSpace(double bottomSpace);

	public double getLeftSpace();

	public void setLeftSpace(double leftSpace);

	public double getRightSpace();

	public void setRightSpace(double rightSpace);

	public void getSpaces(Spacer spacer);

	public void setSpaces(double topSpace, double bottomSpace,
			double leftSpace, double rightSpace);

	public void setSpaces(Spacer spacer);
}