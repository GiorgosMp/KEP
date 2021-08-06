package com.smilias.kep;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smilias.kep.databinding.FragmentApplicationsBinding;
import com.smilias.kep.model.Citizen;
import com.smilias.kep.model.Functions;

import java.io.File;
import java.io.FileOutputStream;


public class ApplicationsFragment extends Fragment {
    private FragmentApplicationsBinding binding;
    private DatabaseReference myRef;
    private Citizen citizen;
    private FirebaseDatabase database;
    private String name,email;

    public ApplicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApplicationsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users/" + LogInActivity.username);
        createCitizen();

        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.application_list));
        binding.listViewApplications.setAdapter(adapter);
        binding.listViewApplications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = binding.listViewApplications.getItemAtPosition(i).toString();
                email = citizen.getEmail();
                switch (item) {
                    case "ΠΙΣΤΟΠΟΙΗΤΙΚΟ ΟΙΚΟΓΕΝΕΙΑΚΗΣ ΚΑΤΑΣΤΑΣΗΣ":
                        name = "pistopoitiko_oikogeniakis_katastasis";
                        Intent intent = new Intent(getActivity(), SignatureActivity.class);
                        intent.putExtra("name",name);
                        intent.putExtra("email",email);
                        startActivity(intent);
                        pdfCreate("ΠΙΣΤΟΠΟΙΗΤΙΚΟ ΟΙΚΟΓΕΝΕΙΑΚΗΣ ΚΑΤΑΣΤΑΣΗΣ",0);
                        break;
                    case "ΠΙΣΤΟΠΟΙΗΤΙΚΟ ΓΕΝΝΗΣΗΣ":
                        name = "pistopoitiko_gennisis";
                        pdfCreate("ΠΙΣΤΟΠΟΙΗΤΙΚΟ ΓΕΝΝΗΣΗΣ", 1);
                        Toast.makeText(getActivity(), "Email sent", Toast.LENGTH_SHORT).show();
                        break;
                    case "ΠΙΣΤΟΠΟΙΗΤΙΚΟ ΕΜΒΟΛΙΑΣΜΟΥ COVID-19":
                        name = "pistopoitiko_emvoliasmou_covid19";
                        pdfCreate("ΠΙΣΤΟΠΟΙΗΤΙΚΟ ΕΜΒΟΛΙΑΣΜΟΥ COVID-19", 1);
                        Toast.makeText(getActivity(), "Email sent", Toast.LENGTH_SHORT).show();
                        break;
                    case "ΠΙΣΤΟΠΟΙΗΤΙΚΟ ΙΘΑΓΕΝΕΙΑΣ":
                        name = "pistopoitiko_ithageneias";
                        Intent intent2 = new Intent(getActivity(), SignatureActivity.class);
                        intent2.putExtra("name",name);
                        intent2.putExtra("email",email);
                        startActivity(intent2);
                        pdfCreate("ΠΙΣΤΟΠΟΙΗΤΙΚΟ ΙΘΑΓΕΝΕΙΑΣ", 0);
                        break;
                    case "ΠΙΣΤΟΠΟΙΗΤΙΚΟ ΕΓΓΥΤΕΡΩΝ ΣΥΓΓΕΝΩΝ":
                        name = "pistopoitiko_eggiterwn_siggenwn";
                        pdfCreate("ΠΙΣΤΟΠΟΙΗΤΙΚΟ ΕΓΓΥΤΕΡΩΝ ΣΥΓΓΕΝΩΝ", 1);
                        Toast.makeText(getActivity(), "Email sent", Toast.LENGTH_SHORT).show();
                        break;
                    case "ΠΙΣΤΟΠΟΙΗΤΙΚΟ ΣΥΜΦΩΝΟΥ ΣΥΜΒΙΩΣΗΣ":
                        name = "pistopoitiko_simfonou_simviosis";
                        Intent intent3 = new Intent(getActivity(), SignatureActivity.class);
                        intent3.putExtra("name",name);
                        intent3.putExtra("email",email);
                        startActivity(intent3);
                        pdfCreate("ΠΙΣΤΟΠΟΙΗΤΙΚΟ ΣΥΜΦΩΝΟΥ ΣΥΜΒΙΩΣΗΣ", 0);
                        break;
                    default:
                        break;
                }
            }
        });
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void createCitizen() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                citizen = dataSnapshot.getValue(Citizen.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }


    private void pdfCreate(String paperTitle, int i) {
        try {
            PrintAttributes.Builder builder = new PrintAttributes.Builder();
            builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
            builder.setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME);
            builder.setMinMargins(PrintAttributes.Margins.NO_MARGINS);
            PrintAttributes attr = builder.build();
            // open a new doc
            PrintedPdfDocument document = new PrintedPdfDocument(getActivity(),
                    attr);

            // start a page
            PdfDocument.Page page = document.startPage(0);

//          draw in page
            Canvas canvas = page.getCanvas();
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            Paint paint = new Paint();

            canvas.drawText(paperTitle, 150, 100, paint);
            canvas.drawText("First name: " + citizen.getFirstName(), 100, 200, paint);
            canvas.drawText("Last name: " + citizen.getLastName(), 100, 230, paint);
            canvas.drawText("Father's name: " + citizen.getFatherName(), 100, 260, paint);
            canvas.drawText("Mother's name: " + citizen.getMotherName(), 100, 290, paint);
            canvas.drawText("Birth date: " + citizen.getBirthDate(), 100, 320, paint);
            canvas.drawText("ID number: " + citizen.getId(), 100, 350, paint);
            canvas.drawText("AMKA number: " + citizen.getAmka(), 100, 380, paint);
            canvas.drawText("Tax number: " + citizen.getTaxNumber(), 100, 410, paint);
            canvas.drawText("Street address: " + citizen.getAddress(), 100, 440, paint);
            canvas.drawText("Email: " + citizen.getEmail(), 100, 470, paint);
            document.finishPage(page);

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/"+ name + ".pdf");
                document.writeTo(new FileOutputStream(file));

//              close the document
                document.close();
                if (i == 1) {
                    Functions functions = new Functions(name, email);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try  {
                                functions.sendemail(1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something happened, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}