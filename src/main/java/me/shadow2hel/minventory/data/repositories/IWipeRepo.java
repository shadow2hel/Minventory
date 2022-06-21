package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.data.Database;

import java.util.Date;
import java.util.List;

public interface IWipeRepo {
    Date createWipe(Date date);
    List<Date> readAllWipes();
    Date readLatestWipe();
}
