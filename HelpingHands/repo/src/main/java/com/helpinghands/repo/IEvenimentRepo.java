package com.helpinghands.repo;

import com.helpinghands.domain.Eveniment;
import com.helpinghands.repo.data.EventOrderOption;

public interface IEvenimentRepo extends IRepo<Eveniment> {
    Eveniment[] getOrderedPaged(EventOrderOption orderOption, int page, int itemsPerPage);
    Eveniment[] getByOrganizer(int organizerId);
    Eveniment[] getByVoluntar(int volId);


}
