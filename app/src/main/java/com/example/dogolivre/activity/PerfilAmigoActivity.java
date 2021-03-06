package com.example.dogolivre.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dogolivre.R;
import com.example.dogolivre.helper.ConfiguracaoFirebase;
import com.example.dogolivre.helper.UsuarioFirebase;
import com.example.dogolivre.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAmigoActivity extends AppCompatActivity {

    private Usuario usuarioSelecionado;
    private Usuario usuarioLogado;
    private Button buttonAcaoPerfil;
    private CircleImageView imagePerfil;
    private TextView textPublicacoes, textSeguidores, textSeguindo;

    private DatabaseReference firebaseRef;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioAmigoRef;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference seguidoresRef;
    private ValueEventListener valueEventListenerPerfilAmigo;

    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        //Configurações iniciais
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        usuariosRef = firebaseRef.child("usuarios");
        seguidoresRef = firebaseRef.child("seguidores");
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        //Inicializar componentes
        inicializarComponentes();

        //Configura toolbar


        //Recuperar usuario selecionado
        Bundle bundle = getIntent().getExtras();
        if( bundle != null ){
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

            //Configura nome do usuário na toolbar

            //Recuperar foto do usuário
            String caminhoFoto = usuarioSelecionado.getCaminhoFoto();
            if( caminhoFoto != null ){
                Uri url = Uri.parse( caminhoFoto );
                Glide.with(PerfilAmigoActivity.this)
                        .load( url )
                        .into( imagePerfil );
            }

        }

    }

    private void recuperarDadosUsuarioLogado(){

        usuarioLogadoRef = usuariosRef.child( idUsuarioLogado );
        usuarioLogadoRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Recupera dados de usuário logado
                        usuarioLogado = dataSnapshot.getValue( Usuario.class );

                        /* Verifica se usuário já está seguindo
                           amigo selecionado
                         */
                        verificaSegueUsuarioAmigo();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    private void verificaSegueUsuarioAmigo(){

        DatabaseReference seguidorRef = seguidoresRef
                .child( idUsuarioLogado )
                .child( usuarioSelecionado.getId() );

        seguidorRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if( dataSnapshot.exists() ){
                            //Já está seguindo
                            Log.i("dadosUsuario", ": Seguindo" );
                            habilitarBotaoSeguir( true );
                        }else {
                            //Ainda não está seguindo
                            Log.i("dadosUsuario", ": seguir" );
                            habilitarBotaoSeguir( false );
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    private void habilitarBotaoSeguir( boolean segueUsuario ){

        if ( segueUsuario ){
            buttonAcaoPerfil.setText("Seguindo");
        }else {

            buttonAcaoPerfil.setText("Seguir");

            //Adiciona evento para seguir usuário
            buttonAcaoPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Salvar seguidor
                    salvarSeguidor(usuarioLogado, usuarioSelecionado);
                }
            });

        }

    }

    private void salvarSeguidor(Usuario uLogado, Usuario uAmigo){


        HashMap<String, Object> dadosAmigo = new HashMap<>();
        dadosAmigo.put("nome", uAmigo.getNome() );
        dadosAmigo.put("caminhoFoto", uAmigo.getCaminhoFoto() );
        DatabaseReference seguidorRef = seguidoresRef
                .child( uLogado.getId() )
                .child( uAmigo.getId() );
        seguidorRef.setValue( dadosAmigo );

        //Alterar botao acao para seguindo
        buttonAcaoPerfil.setText("Seguindo");
        buttonAcaoPerfil.setOnClickListener(null);


    }

    @Override
    protected void onStart() {
        super.onStart();

        //Recuperar dados do amigo selecionado
        recuperarDadosPerfilAmigo();

        //Recuperar dados usuario logado
        recuperarDadosUsuarioLogado();

    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioAmigoRef.removeEventListener( valueEventListenerPerfilAmigo );
    }

    private void recuperarDadosPerfilAmigo(){

        usuarioAmigoRef = usuariosRef.child( usuarioSelecionado.getId() );
        valueEventListenerPerfilAmigo = usuarioAmigoRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Usuario usuario = dataSnapshot.getValue( Usuario.class );

                        String postagens = String.valueOf( usuario.getPostagens() );
                        String seguindo = String.valueOf( usuario.getAdotados() );
                        String seguidores = String.valueOf( usuario.getDoados() );

                        //Configura valores recuperados
                        textPublicacoes.setText( postagens );
                        textSeguidores.setText( seguidores );
                        textSeguindo.setText( seguindo );

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    private void inicializarComponentes(){
        imagePerfil = findViewById(R.id.imagePerfil);
        buttonAcaoPerfil = findViewById(R.id.buttonAcaoPerfil);
        textPublicacoes = findViewById(R.id.textPublicacoes);
        textSeguidores = findViewById(R.id.textAdotados);
        textSeguindo = findViewById(R.id.textDoados);
        buttonAcaoPerfil.setText("Carregando");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}