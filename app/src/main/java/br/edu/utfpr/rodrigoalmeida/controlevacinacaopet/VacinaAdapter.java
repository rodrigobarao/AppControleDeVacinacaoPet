package br.edu.utfpr.rodrigoalmeida.controlevacinacaopet;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class VacinaAdapter extends BaseAdapter {

    private Context context;
    private List<Vacina> vacinas;
    private NumberFormat numberFormat;

    private static class PessoaHolder {
        public TextView textViewValorNome;
        public TextView textViewValorRendaMensal;
        public TextView textViewValorTipo;
    }

    public VacinaAdapter(Context context, List<Vacina> vacinas) {
        this.context = context;
        this.vacinas = vacinas;

        numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    }


    @Override
    public int getCount() {
        return vacinas.size();
    }

    @Override
    public Object getItem(int i) {
        return vacinas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        PessoaHolder holder;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linha_lista_vacinas, viewGroup, false);

            holder = new PessoaHolder();

            holder.textViewValorNome = view.findViewById(R.id.textViewValorNome);
            holder.textViewValorRendaMensal = view.findViewById(R.id.textViewValorRendaMensal);
            holder.textViewValorTipo = view.findViewById(R.id.textViewValorTipo);

            view.setTag(holder);

        } else {
            holder = (PessoaHolder) view.getTag();
        }

        holder.textViewValorNome.setText(vacinas.get(i).getNome());

        //Conversão de float para String para formatar moeda
        String valorVacina = numberFormat.format(vacinas.get(i).getValorVacina());

        holder.textViewValorRendaMensal.setText(valorVacina);

        switch (vacinas.get(i).getTipo()) {
            case Caes_e_Gatos:
                holder.textViewValorTipo.setText(R.string.caes_e_gatos);
                break;

            case Caes:
                holder.textViewValorTipo.setText(R.string.caes);
                break;

            case Gatos:
                holder.textViewValorTipo.setText(R.string.gatos);
                break;
        }

        return view;
    }
}
