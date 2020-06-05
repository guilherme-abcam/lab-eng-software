package com.example.maps;

import android.app.Activity;
import android.content.pm.PackageManager;

import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    public static boolean validate (Activity activity, int requestCode, String ... permissions) {
        List<String> list = new ArrayList<String>();
        for  (String permission: permissions) {
            boolean OK = ContextCompat.checkSelfPermission(activity, permission) ==
                    PackageManager.PERMISSION_GRANTED;

            if (!OK) {
                list.add(permission);
                Toast.makeText(activity, "Permissao Adicionada", Toast.LENGTH_SHORT).show();
            }
        }

        if (list.isEmpty()) {
            // Se estiver certo vai retornar true
            Toast.makeText(activity, "Permissao Vazia", Toast.LENGTH_SHORT).show();
            return true;
        }

        // Cria listta de permissões que falta acesso.
        String[] newPermissions = new String[list.size()];
        list.toArray(newPermissions);

        // Pede permissão para o usuário
        ActivityCompat.requestPermissions(activity, newPermissions, 1);
        return false;
    }
}