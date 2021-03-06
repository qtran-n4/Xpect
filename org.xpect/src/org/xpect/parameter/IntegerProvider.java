package org.xpect.parameter;

import org.xpect.setup.XpectSetupFactory;
import org.xpect.state.Creates;

import com.google.common.base.Preconditions;

@XpectSetupFactory
public class IntegerProvider {

	private final IntegerRegion region;

	public IntegerProvider(IntegerRegion region) {
		Preconditions.checkNotNull(region);
		this.region = region;
	}

	@Creates
	public int getIntegerValue() {
		return region.getIntegerValue();
	}

}
