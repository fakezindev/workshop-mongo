package com.fakezindev.workshopmongo.config;

import com.fakezindev.workshopmongo.domain.Post;
import com.fakezindev.workshopmongo.domain.User;
import com.fakezindev.workshopmongo.dto.AuthorDTO;
import com.fakezindev.workshopmongo.repository.PostRepository;
import com.fakezindev.workshopmongo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.TimeZone;

@Configuration
public class Instantiation implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public void run(String... args) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Limpando as coleções
        userRepository.deleteAll();
        postRepository.deleteAll(); // Adicione isso para limpar os posts antigos também

        User maria = new User(null, "Maria Brown", "maria@gmail.com");
        User alex = new User(null, "Alex Green", "alex@gmail.com");
        User bob = new User(null, "Bob Grey", "bob@gmail.com");

        // PASSO 1: Salvar primeiro para o MongoDB gerar os IDs
        userRepository.saveAll(Arrays.asList(maria, alex, bob));

        // PASSO 2: Agora que maria tem um ID, criamos os posts.
        // O AuthorDTO(maria) agora vai capturar o ID que o banco acabou de criar.
        Post post1 = new Post(null, sdf.parse("21/03/2018"), "Partiu viagem", "Vou viajar para São Paulo. Abraços!", new AuthorDTO(maria));
        Post post2 = new Post(null, sdf.parse("23/03/2018"), "Bom dia", "Acordei feliz hoje", new AuthorDTO(maria));

        postRepository.saveAll(Arrays.asList(post1, post2));

        // PASSO 3: No MongoDB, também precisamos associar os posts ao usuário se houver a lista lá
        maria.getPosts().addAll(Arrays.asList(post1, post2));
        userRepository.save(maria);
    }
}
