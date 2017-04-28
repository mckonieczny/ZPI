package calculator;/*
 * Created by Michał Konieczny on 2017-04-28.
 */

import Jama.Matrix;

import javax.servlet.http.Part;
import java.util.Scanner;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

public class Calculator {

    private static final double ERROR = 0.0001;

    public String calculate(Part inFile, Part outFile) throws Exception {

        Scanner in = new Scanner(inFile.getInputStream());
        Scanner out = new Scanner(outFile.getInputStream());

        int size = parseInt(in.nextLine());

        Matrix A = new Matrix(size, size);
        Matrix B = new Matrix(size, size);

        for (int i=0; i<size; i++) {
            String[] ins = in.nextLine().split(" ");
            String[] outs = out.nextLine().split(" ");
            for (int j=0; j<size; j++) {
                A.set(i, j, parseDouble(ins[j]));
                B.set(i, j, parseDouble(outs[j]));
            }
        }

        Matrix I = A.times(B);

        StringBuilder sb = new StringBuilder();

        sb.append("<br/>Matrix A:");
        sb.append(print(A));

        sb.append("<br/>Matrix A^-1:");
        sb.append(print(B));

        sb.append("<br/>Matrix I:");
        sb.append(print(I));

        sb.append("<br/>Result ");
        sb.append(ones(I) ? "OK" : "ERROR");

        return sb.toString();
    }

    private String print(Matrix M) {

        StringBuilder sb = new StringBuilder();

        sb.append("<table>");
        for (int i=0; i < M.getRowDimension(); i++) {
            sb.append("<tr>");
            for (int j=0; j < M.getRowDimension(); j++) {
                sb.append("<td>");
                sb.append(M.get(i, j));
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");

        return sb.toString();
    }

    private boolean ones(Matrix M) {

        for (int i=0; i < M.getRowDimension(); i++) {
            for (int j=0; j < M.getRowDimension(); j++) {
                if (i == j) {
                    if (abs(M.get(i, j) - 1) > ERROR) {
                        return false;
                    }
                } else {
                    if (abs(M.get(i, j)) > ERROR) {
                        return false;
                    }
                }
            }
        }

        return true;

    }

}
