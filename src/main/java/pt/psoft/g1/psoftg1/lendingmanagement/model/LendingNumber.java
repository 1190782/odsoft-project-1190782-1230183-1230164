package pt.psoft.g1.psoftg1.lendingmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * The {@code LendingNumber} class handles the business logic of the identifier of a {@code Lending}.
 * <p>
 * It stores the year of the lending and a sequencial number, and a string combining these two.
 * @author  rmfranca*/
@Embeddable
public class LendingNumber implements Serializable {

    /**
     * Natural key of a {@code Lending}.
     * <p>
     * The string is constructed based on the values of {@code year} and {@code sequencial} (e.g.: 2024/23).
     */
    @Column(name = "LENDING_NUMBER")
    @NotNull
    @NotBlank
    @Size(min = 6, max = 32)
    private String lendingNumber;

    /**
     * Year component of the {@code LendingNumber}
     */
    @Column(name =  "\"YEAR\"")
    @NotNull
    @NotBlank
    private int year;

    /**
     * Sequential component of the {@code LendingNumber}
     */
    @Column(name = "SEQUENCIAL")
    @NotNull
    @NotBlank
    private int sequential;

    /**
     * Constructs a new {@code LendingNumber} object based on a year and a given sequential number.
     * @param   year        Year component of the {@code LendingNumber}
     * @param   sequential  Sequential component of the {@code LendingNumber}
     * */
    public LendingNumber(int year, int sequential) {
        this.year = year;
        this.sequential = sequential;
        this.lendingNumber = year + "/" + sequential;
    }

    /**
     * Constructs a new {@code LendingNumber} object based on a string.
     * <p>
     * Initialization may fail if the format is not as expected.
     * @param lendingNumber String containing the lending number.
     * */
    public LendingNumber(String lendingNumber){
        int year, sequential;
        try { //TODO: Ricardo: Should this logic be here?
            year        = Integer.parseInt(lendingNumber, 0, 3, 10);
            sequential  = Integer.parseInt(lendingNumber, 5, lendingNumber.length(), 10);
        }catch (NumberFormatException | IndexOutOfBoundsException e){
            return;
        }
        this.year = year;
        this.sequential = sequential;
        this.lendingNumber = year + "/" + sequential;
    }

    /**
     * Constructs a new {@code LendingNumber} object based on a given sequential number.
     * <p>
     * The {@code sequential} value should be retrieved from the count of lendings made in the current year.
     * The {@code year} value is automatically set with {@code LocalDate.now().getYear()}.
     * @param sequential Sequential component of the {@code LendingNumber}
     * */
    public LendingNumber(int sequential) {
        this.year = LocalDate.now().getYear();
        this.sequential = sequential;
        this.lendingNumber = year + "/" + sequential;
    }

    /**Protected empty constructor for ORM only.*/
    public LendingNumber() {}

    public String toString() {
        return this.lendingNumber;
    }

}
