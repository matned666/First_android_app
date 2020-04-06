package com.workspace.carnote.model;

import java.util.Comparator;

public class RecordsDateComparator implements Comparator<Record> {

    @Override
    public int compare(Record o1, Record o2) {
        if(o1.getDate().compareTo(o2.getDate()) < 0) return -1;
        else if (o1.getDate().compareTo(o2.getDate()) == 0) return 0;
        else return 1;
    }
}
