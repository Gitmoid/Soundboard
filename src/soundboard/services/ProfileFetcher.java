package soundboard.services;

import soundboard.domain.Sound;

import java.util.List;

public interface ProfileFetcher {
    List<Sound> getOnlineSounds();
}
