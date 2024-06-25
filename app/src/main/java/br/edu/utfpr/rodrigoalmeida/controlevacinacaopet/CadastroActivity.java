package br.edu.utfpr.rodrigoalmeida.controlevacinacaopet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CadastroActivity extends AppCompatActivity {
    public static final String MODO = "MODO";
    public static final int NOVO = 1;
    public static final int EDITAR = 2;
    private int modo;
    private String nomeOriginal;
    public static final String NOME = "nome";
    public static final String TELEFONE = "telefone";
    public static final String NOME_PET = "nomePet";
    public static final String RACA_PET = "racaPet";
    public static final String DATA_NASCIMENTO = "dataNascimento";
    public static final String SEXO = "sexo";
    public static final String TIPO = "tipo";

    public static final String SUGERIR_TIPO = "SUGERIR_TIPO";
    public static final String ULTIMO_TIPO = "ULTIMO_TIPO"; //recuperar o ultimo valor

    private boolean sugerirTipo = false;
    private int ultimoTipo = 0;

    private EditText editTextNome, editTextTelefone, editTextNomePet, editTextRacaPet, editTextDataNascimento;
    private CheckBox checkBoxCachorro, checkBoxGato;
    private RadioGroup radioGroupSexo;
    private Spinner spinnerSexoAnimal;

    public static void novaVacina(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(activity, CadastroActivity.class);
        intent.putExtra(MODO, NOVO);

        launcher.launch(intent);
    }

    public static void editarVacina(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher, Vacina vacina) {
        Intent intent = new Intent(activity, CadastroActivity.class);
        intent.putExtra(MODO, EDITAR);
        intent.putExtra(NOME, vacina.getNome());
        intent.putExtra(TELEFONE, vacina.getTelefone());
        intent.putExtra(NOME_PET, vacina.getNomePet());
        intent.putExtra(RACA_PET, vacina.getRacaPet());
        intent.putExtra(DATA_NASCIMENTO, vacina.getDataNascimento());
        intent.putExtra(SEXO, vacina.getSexo());
        intent.putExtra(TIPO, vacina.getTipo());

        launcher.launch(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(getString(R.string.cadastro_de_vacina));

        editTextNome = findViewById(R.id.editTextNome);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextNomePet = findViewById(R.id.editTextNomePet);
        editTextRacaPet = findViewById(R.id.editTextRacaPet);
        editTextDataNascimento = findViewById(R.id.editTextDataNascimento);
        checkBoxCachorro = findViewById(R.id.checkBoxCachorro);
        checkBoxGato = findViewById(R.id.checkBoxGato);
        radioGroupSexo = findViewById(R.id.radioGroupSexo);
        spinnerSexoAnimal = findViewById(R.id.spinnerSexoAnimal);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        lerSugerirTipo();
        lerUltimoTipo();

        if (bundle != null) {
            modo = bundle.getInt(MODO, NOVO);
            if (modo == NOVO) {
                setTitle(getString(R.string.nova_vacina));

                if (sugerirTipo) {
                    carregarDadosUsuario();
                }
            } else if (modo == EDITAR) {
                setTitle(getString(R.string.editar_vacina));
                nomeOriginal = bundle.getString(NOME);
                editTextNome.setText(nomeOriginal);
                editTextTelefone.setText(bundle.getString(TELEFONE));
                editTextNomePet.setText(bundle.getString(NOME_PET));
                editTextRacaPet.setText(bundle.getString(RACA_PET));
                editTextDataNascimento.setText(bundle.getString(DATA_NASCIMENTO));
                spinnerSexoAnimal.setSelection(bundle.getInt(SEXO));
                editTextNome.setSelection(editTextNome.getText().length());
            }
        }

        sexoAnimalSpinner();
    }

    private void sexoAnimalSpinner() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add(getString(R.string.spinner_masculino));
        lista.add(getString(R.string.spinner_feminino));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, lista);

        spinnerSexoAnimal.setAdapter(adapter);
    }

    public void limparCampos() {
        editTextNome.setText(null);
        editTextTelefone.setText(null);
        editTextNomePet.setText(null);
        editTextRacaPet.setText(null);
        editTextDataNascimento.setText(null);
        checkBoxCachorro.setChecked(false);
        checkBoxGato.setChecked(false);
        radioGroupSexo.clearCheck();
        spinnerSexoAnimal.setSelection(0);

        editTextNome.requestFocus();
        editTextNome.selectAll();

        Toast.makeText(this, R.string.campos_limpos, Toast.LENGTH_SHORT).show();
    }

    public void cancelar() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void salvarConteudo() {
        String nome = editTextNome.getText().toString();
        String telefone = editTextTelefone.getText().toString();
        String nomePet = editTextNomePet.getText().toString();
        String racaPet = editTextRacaPet.getText().toString();
        String dataNascimento = editTextDataNascimento.getText().toString();
        String sexo = (String) spinnerSexoAnimal.getSelectedItem();

        String mensagem;

        if (sexo != null) {
            mensagem = sexo + getString(R.string.foi_selecionado);
        } else {
            mensagem = getString(R.string.nenhum_sexo_selecionado);
        }

        if (nome.trim().isEmpty()) {
            Toast.makeText(this, R.string.erro_nome, Toast.LENGTH_SHORT).show();
            editTextNome.requestFocus();
            return;
        }
        if (telefone.trim().isEmpty()) {
            Toast.makeText(this, R.string.erro_telefone, Toast.LENGTH_SHORT).show();
            editTextTelefone.requestFocus();
            return;
        }
        if (nomePet.trim().isEmpty()) {
            Toast.makeText(this, R.string.erro_nome_pet, Toast.LENGTH_SHORT).show();
            editTextNomePet.requestFocus();
            return;
        }
        if (racaPet.trim().isEmpty()) {
            Toast.makeText(this, R.string.erro_raca_pet, Toast.LENGTH_SHORT).show();
            editTextRacaPet.requestFocus();
            return;
        }
        if (dataNascimento.trim().isEmpty()) {
            Toast.makeText(this, R.string.erro_data_nascimento, Toast.LENGTH_SHORT).show();
            editTextDataNascimento.requestFocus();
            return;
        }

        int botaoSelecionado = radioGroupSexo.getCheckedRadioButtonId();

        if (botaoSelecionado == R.id.radioButtonMasculino) {
            mensagem = getString(R.string.masculino) + getString(R.string.foi_selecionado);
        } else if (botaoSelecionado == R.id.radioButtonFeminino) {
            mensagem = getString(R.string.feminino) + getString(R.string.foi_selecionado);
        } else {
            mensagem = getString(R.string.nenhum_sexo_selecionado);
        }

        if (checkBoxCachorro.isChecked()) {
            mensagem += getString(R.string.cachorro) + "\n";
        }
        if (checkBoxGato.isChecked()) {
            mensagem += getString(R.string.gato) + "\n";
        }
        if (!checkBoxCachorro.isChecked() && !checkBoxGato.isChecked()) {
            mensagem += getString(R.string.nenhum_opcao_selecionada);
        }

        salvarDadosUsuario();

        Intent intent = new Intent();

        intent.putExtra(NOME, nome);
        intent.putExtra(TELEFONE, telefone);
        intent.putExtra(NOME_PET, nomePet);
        intent.putExtra(RACA_PET, racaPet);
        intent.putExtra(DATA_NASCIMENTO, dataNascimento);
        intent.putExtra(SEXO, sexo);

        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vacina_opcoes, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menuItemSugerirTipo);
        item.setChecked(sugerirTipo);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemSalvar) {
            salvarConteudo();
            return true;
        } else if (idMenuItem == R.id.menuItemSugerirTipo) {
            boolean valor = !item.isChecked();

            salvarSugerirTipo(valor);
            item.setChecked(valor);
            if (sugerirTipo) {
                carregarDadosUsuario();
                return true;
            }
        } else if (idMenuItem == android.R.id.home) {
            cancelar();
            return true;
        } else if (idMenuItem == R.id.menuItemLimpar) {
            limparCampos();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void lerSugerirTipo() {
        SharedPreferences shared = getSharedPreferences(PrincipalActivity.ARQUIVO, Context.MODE_PRIVATE);
        sugerirTipo = shared.getBoolean(SUGERIR_TIPO, sugerirTipo);
    }

    private void salvarSugerirTipo(boolean novoValor) {
        SharedPreferences shared = getSharedPreferences(PrincipalActivity.ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putBoolean(SUGERIR_TIPO, novoValor);
        editor.commit();

        sugerirTipo = novoValor;
    }

    private void lerUltimoTipo() {
        SharedPreferences shared = getSharedPreferences(PrincipalActivity.ARQUIVO, Context.MODE_PRIVATE);
        ultimoTipo = shared.getInt(ULTIMO_TIPO, ultimoTipo);
    }

    private void salvarUltimoTipo(int novoValor) {
        SharedPreferences shared = getSharedPreferences(PrincipalActivity.ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putInt(ULTIMO_TIPO, novoValor);
        editor.commit();

        ultimoTipo = novoValor;
    }

    private void salvarDadosUsuario() {
        SharedPreferences shared = getSharedPreferences(PrincipalActivity.ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putString(NOME, editTextNome.getText().toString());
        editor.putString(TELEFONE, editTextTelefone.getText().toString());
        editor.putString(NOME_PET, editTextNomePet.getText().toString());
        editor.putString(RACA_PET, editTextRacaPet.getText().toString());
        editor.putString(DATA_NASCIMENTO, editTextDataNascimento.getText().toString());
        editor.putInt(SEXO, spinnerSexoAnimal.getSelectedItemPosition());

        editor.commit();
    }

    private void carregarDadosUsuario() {
        SharedPreferences shared = getSharedPreferences(PrincipalActivity.ARQUIVO, Context.MODE_PRIVATE);

        editTextNome.setText(shared.getString(NOME, ""));
        editTextTelefone.setText(shared.getString(TELEFONE, ""));
        editTextNomePet.setText(shared.getString(NOME_PET, ""));
        editTextRacaPet.setText(shared.getString(RACA_PET, ""));
        editTextDataNascimento.setText(shared.getString(DATA_NASCIMENTO, ""));
        spinnerSexoAnimal.setSelection(shared.getInt(SEXO, 0));
    }
}
