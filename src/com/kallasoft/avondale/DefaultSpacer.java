package com.kallasoft.avondale;

public class DefaultSpacer extends AbstractSpacer
{
	public DefaultSpacer()
	{
		this(0, 0, 0, 0);
	}

	public DefaultSpacer(double topSpace, double bottomSpace, double leftSpace,
			double rightSpace)
	{
		super(topSpace, bottomSpace, leftSpace, rightSpace);
	}

	public DefaultSpacer(Spacer spacer)
	{
		this(spacer.getTopSpace(), spacer.getBottomSpace(), spacer
				.getLeftSpace(), spacer.getRightSpace());
	}
}