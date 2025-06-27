package com.avegus.catsservice.broker;

import com.avegus.commons.model.*;

public interface CatConsumer {
    void onCatAddRequest(AddCatRequest request);
    void onCatLikeRequest(CatIdDto catId);
    void onCatDislikeRequest(CatIdDto catId);
    void onCatDeleteRequest(CatIdWithUserId request);
    void onUserCatByCatIdRequest(CatIdWithUserId request);
    void onCatByUserIdRequest(UserIdDto userId);
    void onAllUserCatsRequest(UserIdDto userId);
}
