package br.edu.utfpr.rodrigoalmeida.controlevacinacaopet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PrincipalActivity extends AppCompatActivity {

    private ListView listViewVacinas;
    private ArrayAdapter<Vacina> listaAdapter;
    private ArrayList<Vacina> listaVacinas;
    private ActionMode actionMode;
    private View viewSelecionada;
    private int posicaoSelecionada = -1;

    public static final String ARQUIVO = "br.edu.utfpr.rodrigoalmeidafarias.botaoup.PREFERENCIAIS";

    public static final String ORDENACAO_ASCENDENTE = "ORDENACAO_ASCENDENTE";
    public boolean ordenacaoAscentende = true;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.principal_item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int idMenuItem = item.getItemId();
                if (idMenuItem == R.id.menuItemEditar) {
                    editarVacina();
                    mode.finish();
                    return true;
                } else if (idMenuItem == R.id.menuItemExcluir) {
                    excluirVacina();
                    mode.finish();
                    return true;
                } else {
                    return false;
                }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            if(viewSelecionada !=null) {
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }

            actionMode = null;
            viewSelecionada = null;

            listViewVacinas.setEnabled(true);


        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        setTitle(getString(R.string.carteira_de_vacinacao));


        listViewVacinas = findViewById(R.id.listViewVacinas);

        listViewVacinas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewVacinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicaoSelecionada = position;
                editarVacina();
            }
        });

        listViewVacinas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view,
                                           int position,
                                           long id) {

                if (actionMode != null) {
                    return false;

                }

                posicaoSelecionada = position;

                view.setBackgroundColor(Color.LTGRAY);

                viewSelecionada = view;

                listViewVacinas.setEnabled(false);

                actionMode = startSupportActionMode(mActionModeCallback);

                return false;
            }
        });

        lerPreferenciaOrdenacaoAscendente();
        popularLista();

    }

    private void excluirVacina() {
        listaVacinas.remove(posicaoSelecionada);
        listaAdapter.notifyDataSetChanged();
    }

    ActivityResultLauncher<Intent> launcherEditarVacina = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {

                        Intent intent = result.getData();

                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {
                            String nome = bundle.getString(CadastroActivity.NOME);
                            String dataNascimento = bundle.getString(CadastroActivity.DATA_NASCIMENTO);
                            String telefone = bundle.getString(CadastroActivity.TELEFONE);
                            String sexo = bundle.getString(CadastroActivity.SEXO);
                            String racaPet = bundle.getString(CadastroActivity.RACA_PET);
                            String nomePet = bundle.getString(CadastroActivity.NOME_PET);

                            Vacina vacina = listaVacinas.get(posicaoSelecionada);
                            vacina.setNome(nome);
                            vacina.setDataNascimento(dataNascimento);
                            vacina.setTelefone(telefone);
                            vacina.setSexo(sexo);
                            vacina.setRacaPet(racaPet);
                            vacina.setNomePet(nomePet);

                            posicaoSelecionada = -1;

                            ordenarLista();
                        }
                    }
                }
            });
    private void editarVacina() {

        Vacina vacina = listaVacinas.get(posicaoSelecionada);

        CadastroActivity.editarVacina(this, launcherEditarVacina, vacina);

    }

    private void popularLista() {
        listaVacinas = new ArrayList<>();

        listaAdapter = new ArrayAdapter<>(this,
                                                    android.R.layout.simple_list_item_1,
                                                    listaVacinas);

        listViewVacinas.setAdapter(listaAdapter);
    }

    ActivityResultLauncher<Intent> launcherNovaVacina = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {

                        Intent intent = result.getData();

                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {
                         String nome = bundle.getString(CadastroActivity.NOME);
                         String dataNascimento = bundle.getString(CadastroActivity.DATA_NASCIMENTO);
                         String telefone = bundle.getString(CadastroActivity.TELEFONE);
                         String sexo = bundle.getString(CadastroActivity.SEXO);
                         String racaPet = bundle.getString(CadastroActivity.RACA_PET);
                         String nomePet = bundle.getString(CadastroActivity.NOME_PET);

                         Vacina vacina = new Vacina(nome, dataNascimento, telefone, sexo, racaPet, nomePet);


                         listaVacinas.add(vacina);


                        ordenarLista();

                        }
                    }
                }
            });


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_opcoes, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem menuItemOrdenacao = menu.findItem(R.id.menuItemOrdenacao);

        atualizarIconeOrdenacao(menuItemOrdenacao);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemAdicionar) {
            CadastroActivity.novaVacina(this, launcherNovaVacina);
            return true;

        } else
            if (idMenuItem == R.id.menuItemOrdenacao) {
                salvarPreferenciaOrdenacaoAscentende(!ordenacaoAscentende);
                atualizarIconeOrdenacao(item);
                ordenarLista();
                return true;
            } else
                if (idMenuItem == R.id.menuItemSobre) {
            SobreActivity.nova(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void atualizarIconeOrdenacao(MenuItem menuItemOrdenacao) {
        if (ordenacaoAscentende){
            menuItemOrdenacao.setIcon(R.drawable.ic_action_ordenacao_ascendente);
        } else {
            menuItemOrdenacao.setIcon(R.drawable.ic_action_ordenacao_descendente);
        }
    }
    private void ordenarLista() {
        if (ordenacaoAscentende) {
            Collections.sort(listaVacinas, Vacina.ordenacaoCrescente);
        } else {
            Collections.sort(listaVacinas, Vacina.ordenacaoDecrescente);
        }

        listaAdapter.notifyDataSetChanged();

    }
    private void lerPreferenciaOrdenacaoAscendente() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        ordenacaoAscentende = shared.getBoolean(ORDENACAO_ASCENDENTE, ordenacaoAscentende);
    }

    private void salvarPreferenciaOrdenacaoAscentende(boolean novoValor) {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(ORDENACAO_ASCENDENTE, novoValor);
        editor.commit();
        ordenacaoAscentende = novoValor;

    }
}
