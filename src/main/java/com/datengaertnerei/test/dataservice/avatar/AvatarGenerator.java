package com.datengaertnerei.test.dataservice.avatar;

import java.util.Random;

import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.eightbit.EightBitAvatar;

public class AvatarGenerator implements IAvatarGenerator {
	private Random rnd;

	private AvatarGenerator() {
		rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
	}

	@Override
	public byte[] getAvatar(boolean female) {
		Avatar avatar = null;

		if (female) {
			avatar = EightBitAvatar.newFemaleAvatarBuilder().build();
		} else {
			avatar = EightBitAvatar.newMaleAvatarBuilder().build();
		}

		return avatar.createAsPngBytes(rnd.nextLong());
	}

}
