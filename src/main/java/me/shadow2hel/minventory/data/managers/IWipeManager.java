package me.shadow2hel.minventory.data.managers;

import java.util.Date;
import java.util.List;

public interface IWipeManager {
    Date createWipe(Date date);
    List<Date> readAllWipes();
    Date readLatestWipe();
}
