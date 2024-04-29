package pt.psoft.g1.psoftg1.readermanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;


@Embeddable
public class ReaderNumber implements Serializable {
    @Column(name = "READER_NUMBER")
    private String readerNumber;

    public ReaderNumber(int year, int number) {
        this.readerNumber = year + "/" + number;
    }

    protected ReaderNumber() {}
}
