package com.lb12.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FloatTextureData;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.omg.PortableInterceptor.Interceptor;

import java.util.Random;

public class FlappyZombie extends ApplicationAdapter {

	//public static float MINHA_PROPORCAO = WIDTH_IMAGEM / Gdx.graphics.getWidth();

	private SpriteBatch spriteBatch;
	private Texture[] zumbi;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Texture gameOver;
	private Random nRandom;
	private BitmapFont fonte;

	private float larguraTela;
	private float alturaTela;
	private float posicaoZumbi;
	private float variacao  = 0;
	private float velocidadeQueda = 0;
	private float alturaRandomica;
	private float posicaoInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float deltaTime;
	private float espacoEntreCanos;

	private int estadoJogo = 0;
	private int pontuacao = 0;
	private float alturaGameOver;
	private float larguraGameOver;
	private boolean marcouPonto;
	private Circle zumbiCirculo;
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;

	//Controles de Camera
	private OrthographicCamera camera;
	private Viewport viewPort;

	//private ShapeRenderer shapeRenderer;

	private final float VIRTUAL_WIDTH  = 768 ;
	private final float VIRTUAL_HEIGHT = 1024;

	@Override
	public void create () {
		//Gdx.app.log( "Create", "Game initialized" );

		larguraTela = VIRTUAL_WIDTH;
		alturaTela  = VIRTUAL_HEIGHT;
		alturaGameOver  = alturaTela / 8;
		larguraGameOver = larguraTela  / 2;

		posicaoZumbi = larguraTela / 6;

		espacoEntreCanos = alturaTela / 3;

		posicaoInicialVertical = alturaTela / 2;
		posicaoMovimentoCanoHorizontal = larguraTela + larguraTela / 2;

		nRandom = new Random();

		fonte   = new BitmapFont();
		fonte.setColor( Color.WHITE );
		fonte.getData().setScale( 6 );

		spriteBatch   = new SpriteBatch();
		//shapeRenderer = new ShapeRenderer();

		zumbiCirculo       = new Circle();

		canoTopo  = new Texture("cano_topo.png");
		canoBaixo = new Texture("cano_baixo.png");

		zumbi      = new Texture[ 3 ];
		zumbi[ 0 ] = new Texture( "zumbi1.png" );
		zumbi[ 1 ] = new Texture( "zumbi2.png" );
		zumbi[ 2 ] = new Texture( "zumbi3.png" );

		camera = new OrthographicCamera();
		camera.position.set( VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0 );

		viewPort = new StretchViewport( VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera );

		fundo    = new Texture( "fundo.png"     );
		gameOver = new Texture( "game_over.png" );

		retanguloCanoTopo = new Rectangle( posicaoMovimentoCanoHorizontal,
										alturaTela / 2 + ( espacoEntreCanos / 2 ) + alturaRandomica,
											canoTopo.getWidth(),
										    canoTopo.getHeight() );

		retanguloCanoBaixo = new Rectangle( posicaoMovimentoCanoHorizontal,
				 						-( espacoEntreCanos / 2 ) + alturaRandomica,
											canoBaixo.getWidth(),
											canoBaixo.getHeight() );

	}

	@Override
	public void render () {
		//Gdx.app.log( "Render", "Rendering: "  );

		camera.update();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_COLOR_BUFFER_BIT);

		deltaTime = Gdx.graphics.getDeltaTime();

		spriteBatch.begin();

		spriteBatch.setProjectionMatrix( camera.combined );

		spriteBatch.draw(fundo, 0, 0, larguraTela, alturaTela);
		spriteBatch.draw(zumbi[(int) variacao], posicaoZumbi , posicaoInicialVertical);
		spriteBatch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaTela / 2 + (espacoEntreCanos / 2) + alturaRandomica);
		spriteBatch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, -(espacoEntreCanos / 2) + alturaRandomica);

		variacao += deltaTime * 5;

		if ( variacao > 2 )
			variacao = 0;

		if ( estadoJogo != 0 ) { //Jogo sendo executado

			velocidadeQueda += 0.7;

			if ( posicaoInicialVertical < 0 )
				estadoJogo = 2;

			if (posicaoInicialVertical > 0 || velocidadeQueda < 0)
				posicaoInicialVertical -= velocidadeQueda;

			if( estadoJogo == 1 ) {
				posicaoMovimentoCanoHorizontal -= deltaTime * 420;

				if (posicaoMovimentoCanoHorizontal <= -canoTopo.getWidth()) {
					posicaoMovimentoCanoHorizontal = larguraTela + canoTopo.getWidth();
					alturaRandomica = nRandom.nextInt(Gdx.graphics.getHeight() / 5 ) - espacoEntreCanos;
					marcouPonto = false;
				}

				if (posicaoMovimentoCanoHorizontal < posicaoZumbi && !marcouPonto) {
					marcouPonto = true;
					pontuacao++;
				}

				if ( posicaoInicialVertical < alturaTela - zumbi[0].getHeight() && Gdx.input.justTouched() ){
					velocidadeQueda = -14;
				}

			}else{ //Game Over

				spriteBatch.draw( gameOver,
						       larguraTela / 2 - larguraGameOver / 2,
						       alturaTela  / 2,
						          larguraGameOver,
						          alturaGameOver );

				if( Gdx.input.justTouched() ){

					estadoJogo      = 0;
					pontuacao       = 0;
					velocidadeQueda = 0;
					posicaoInicialVertical = alturaTela / 2;
					posicaoMovimentoCanoHorizontal = larguraTela + canoTopo.getWidth();
					marcouPonto = false;

				}

			}

		}else if( Gdx.input.justTouched() ){ // Tela Inicial
			estadoJogo = 1;
			velocidadeQueda = -14;
		}

		fonte.draw( spriteBatch 				 ,
					String.valueOf( pontuacao )  ,
				 larguraTela / 2				 ,
				 alturaTela - alturaTela / 12 );

		spriteBatch.end();

		zumbiCirculo.set( posicaoZumbi           + zumbi[0].getWidth()  / 2,
						  posicaoInicialVertical + zumbi[0].getHeight() / 2,
				      zumbi[0].getHeight() / 2 						  );

		retanguloCanoTopo.setPosition( posicaoMovimentoCanoHorizontal, alturaTela / 2 + (espacoEntreCanos / 2) + alturaRandomica );
		retanguloCanoBaixo.setPosition( posicaoMovimentoCanoHorizontal, -(espacoEntreCanos / 2) + alturaRandomica );

		//Teste de colisão
		if( Intersector.overlaps( zumbiCirculo, retanguloCanoBaixo ) ||
			Intersector.overlaps( zumbiCirculo, retanguloCanoTopo  ) ||
			posicaoZumbi < 0 || posicaoZumbi > alturaTela            ){
			estadoJogo = 2;
		}

	}

	@Override
	public void resize(int width, int height) {
		viewPort.update( width, height );
	}
}
